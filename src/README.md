
# Smart Parking Lot (Spring Boot + Gradle + H2)

Java 17, Spring Boot 3.3, Spring Data JPA, Gradle. No Lombok. Uses an in-memory H2 database.

## Setup

Scripts for tables


CREATE TABLE parking_spot (
id            BIGSERIAL PRIMARY KEY,
floor_number  INT         NOT NULL,
spot_number   INT         NOT NULL,
spot_type     VARCHAR(10) NOT NULL,            -- SMALL | MEDIUM | LARGE
status        VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE | OCCUPIED | OUT_OF_SERVICE
version       BIGINT      NOT NULL DEFAULT 0,
UNIQUE (floor_number, spot_number)
);

CREATE INDEX idx_spot_alloc ON parking_spot (spot_type, floor_number, spot_number)
WHERE status = 'AVAILABLE';

CREATE TABLE rate_card (
vehicle_type   VARCHAR(15) PRIMARY KEY,        -- MOTORCYCLE | CAR | BUS
hourly_rate    NUMERIC(10,2) NOT NULL,
daily_cap      NUMERIC(10,2) NOT NULL,
grace_minutes  INT           NOT NULL DEFAULT 10
);

CREATE TABLE parking_ticket (
id             UUID PRIMARY KEY,
vehicle_number VARCHAR(20)  NOT NULL,
vehicle_type   VARCHAR(15)  NOT NULL,
spot_id        BIGINT       NOT NULL REFERENCES parking_spot(id),
entry_time     TIMESTAMPTZ  NOT NULL,
exit_time      TIMESTAMPTZ,
fee            NUMERIC(10,2),
status         VARCHAR(15)  NOT NULL            -- ACTIVE | COMPLETED
);

CREATE UNIQUE INDEX ux_active_ticket_vehicle
ON parking_ticket (vehicle_number) WHERE status = 'ACTIVE';
CREATE INDEX idx_ticket_spot ON parking_ticket (spot_id);

-- Rates
INSERT INTO rate_card (vehicle_type, hourly_rate, daily_cap, grace_minutes) VALUES ('MOTORCYCLE', 20.00, 100.00, 10);
INSERT INTO rate_card (vehicle_type, hourly_rate, daily_cap, grace_minutes) VALUES ('CAR', 40.00, 300.00, 10);
INSERT INTO rate_card (vehicle_type, hourly_rate, daily_cap, grace_minutes) VALUES ('BUS', 100.00, 800.00, 10);

-- Parking spots: 3 floors, each with spots 1-10 SMALL, 11-30 MEDIUM, 31-35 LARGE
INSERT INTO parking_spot (floor_number, spot_number, spot_type, status)
SELECT f.X, s.X, 'SMALL', 'AVAILABLE' FROM SYSTEM_RANGE(1, 3) f, SYSTEM_RANGE(1, 10) s;

INSERT INTO parking_spot (floor_number, spot_number, spot_type, status)
SELECT f.X, s.X, 'MEDIUM', 'AVAILABLE' FROM SYSTEM_RANGE(1, 3) f, SYSTEM_RANGE(11, 30) s;

INSERT INTO parking_spot (floor_number, spot_number, spot_type, status)
SELECT f.X, s.X, 'LARGE', 'AVAILABLE' FROM SYSTEM_RANGE(1, 3) f, SYSTEM_RANGE(31, 35) s;
Generate the Gradle wrapper once (needs Gradle installed), or open the project in IntelliJ as a Gradle project:
```bash
gradle wrapper
```

## Run
```bash
./gradlew bootRun
```
On startup the lot and rates are loaded from `src/main/resources/data.sql`
(3 floors: 10 SMALL, 20 MEDIUM, 5 LARGE each; rates for MOTORCYCLE, CAR and BUS).
Edit that file to change the layout or pricing. Data is in memory, so it resets on every restart.

## H2 console
Open http://localhost:8080/h2-console and connect with:
- JDBC URL: `jdbc:h2:mem:parking`
- User: `sa`
- Password: (empty)

## Test
```bash
./gradlew test
```

## API
| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/parking/entry | Allocate a spot and issue a ticket |
| POST | /api/v1/parking/exit/{ticketId} | Close ticket, compute fee, free the spot |
| GET  | /api/v1/parking/tickets/{ticketId} | Look up a ticket |
| GET  | /api/v1/parking/availability | Available and total spots per type |

```bash
curl -X POST localhost:8080/api/v1/parking/entry \
  -H 'Content-Type: application/json' \
  -d '{"vehicleNumber":"KA01AB1234","vehicleType":"CAR"}'

curl -X POST localhost:8080/api/v1/parking/exit/<ticketId>
curl localhost:8080/api/v1/parking/availability
```

vehicleType: MOTORCYCLE | CAR | BUS

## Rules
- Allocation: smallest compatible spot type first, then lowest floor, then lowest spot number.
  Motorcycle: SMALL > MEDIUM > LARGE. Car: MEDIUM > LARGE. Bus: LARGE.
- Fee: free within grace minutes; otherwise per started hour; each 24h block capped at the daily cap.
- Rates live in the `rate_card` table.