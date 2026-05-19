## Backend RentalAPI steps explanation

**Lab1** - Create project structure using Spring Boot , Flyway migrations and PostgreSQL database. Creating the first model , repository, service, web layers. Created Accommodation, Host, Country entities and DTOs.Added basic CRUD functionalities, validation, exception handler and tested with SwaggerUI.

**Lab2** - Added functionalities and endpoints in the controller for: Listing and filtering accommodations with pagination, Using projections short and extended accommodation display, Using EntityGraph displaying accommodation information with fewer queries, Basic database accommodation view, Materialized database view for stats per category, Scheduled event for refreshing
the materialized view, Event handling with events and listener for renting an accommodation room, Event and listener for fully booked accommodations,
Logging the activity for renting and fully booked accommodations in an activity log database table.

**Lab3** - Added CORS configuration into the security configuration file
so the frontend can access the API endpoints and fixed some security issues.

**Lab4** - Add CRUD operations for Host and Country and update security so the access on these is 
based on roles.