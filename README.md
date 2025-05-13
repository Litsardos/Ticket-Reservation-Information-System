# 🎫 Ticket-Reservation Information System
_A HY-360 “Files & Databases” course project – Winter Term 2023/24_  
Department of Computer Science, **University of Crete**

This repository (`ticket-reservation-system`) contains the full-stack implementation
(database, backend and web interface) of an information system that manages
on-line ticket reservations for a concert hall.

Team size: three undergraduate students.  
Deadline: **08 December 2024**.

---

## Table of Contents
1. Project Overview  
2. Feature Set  
3. Component Breakdown  
4. Database Schema  
5. Typical Workflows  

---

## 1 · Project Overview
Customers browse upcoming events, choose ticket type and quantity, and pay by
credit card. Venue administrators can create, cancel or update events, monitor
reservations and view revenue analytics.

Design goals  
• Data integrity for seats & payments  
• Real-time seat availability  
• Modular architecture that separates data layer, business logic and UI  

System layers  
• **MariaDB/MySQL** relational database  
• **Java 17** backend communicating via **JDBC**  
• Web UI built with **Java Servlets + JSP**

---

## 2 · Feature Set
### End-User
• Customer sign-up / login  
• Event catalogue with filters (date, genre, availability)  
• Real-time seat availability per ticket category (VIP / GA / Standing)  
• Secure reservation & mock payment gateway  
• E-mail confirmation with unique reservation code  
• Reservation cancellation with refund or fee (configurable)

### Administrator
• CRUD on events and ticket categories  
• Bulk import of events via CSV  
• Event cancellation ⇒ automatic mass-refund  
• Dashboard KPIs  
  – Seats sold vs capacity  
  – Revenue per event / ticket type  
  – Most popular & highest-grossing events  
  – Time-series of reservations

---

## 3 · Component Breakdown
| Package / Folder           | Responsibility |
|----------------------------|----------------|
| `com.ticket.servlet`       | HTTP controllers & session handling |
| `com.ticket.service`       | Business logic (booking, refunds) |
| `com.ticket.dao`           | JDBC data-access layer |
| `com.ticket.model`         | POJOs / DTOs (Event, Customer, Reservation …) |
| `sql/`                     | `schema.sql`, `sample_data.sql`, `procedures.sql` |
| `webapp/`                  | JSP pages, CSS, JavaScript validation |
| `docs/`                    | `er_diagram.png`, sequence diagrams, reports |

---

## 4 · Database Schema
### Core Entities
| Entity        | PK / FK                  | Main Attributes |
|---------------|--------------------------|-----------------|
| Event         | `event_id` (PK)          | name, date, time, type, capacity |
| Customer      | `customer_id` (PK)       | full_name, email, cc_last4, cc_token |
| Ticket        | (`event_id`, `type`) PK  | price, seats_available |
| Reservation   | `reservation_id` (PK)    | customer_id FK, event_id FK, type, qty, amount_paid, ts_booked |

Full ER diagram: `docs/er_diagram.png`.  
All relations are in **3NF**; proofs are included in the Phase I report.

Key stored procedures  
* `sp_book_tickets` – availability check → charge → insert reservation  
* `sp_cancel_reservation` – refund logic & seat update  
* `sp_cancel_event` – cancel event and mass-refund all linked reservations

---

## 5 · Typical Workflows
1. **Booking**  
   Customer → select event/ticket → servlet calls `sp_book_tickets` → returns confirmation code.  
2. **Cancellation**  
   Customer → submit code → policy check → `sp_cancel_reservation` → refund.  
3. **Event Cancellation (Admin)**  
   Admin → set status “Cancelled” → trigger runs `sp_cancel_event` → automatic refunds.

Sequence diagrams are stored in `docs/sequence_booking.puml`.
