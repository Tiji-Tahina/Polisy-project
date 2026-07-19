# TODO

## Done
- [x] Set up MVVM architecture (Model → Repository → ViewModel → Compose)
- [x] Create data models (Driver, Vehicle, Infraction)
- [x] Set up Retrofit + PostgREST API layer
- [x] Create InfractionRepository for data abstraction
- [x] Build FormViewModel with form state management
- [x] Build FormScreen (3-section Compose form)
- [x] Wire up MainActivity
- [x] Generate PostgreSQL schema (schema.sql)

## Next Steps
- [ ] Deploy PostgreSQL + PostgREST on Docker
- [ ] Test API endpoints with PostgREST
- [ ] Update BASE_URL in RetrofitInstance.kt to match deployed server
- [ ] Add input validation (empty fields, phone format, year range)
- [ ] Add loading spinner during API calls
- [ ] Handle duplicate driver/vehicle (search before insert)
- [ ] Add list view to see existing infractions
- [ ] Add edit/delete functionality from list
- [ ] Add search/filter for infractions
- [ ] Add authentication (JWT via PostgREST role)
- [ ] Unit tests for ViewModel + Repository
- [ ] Edge-to-edge padding fix for scrollable form
