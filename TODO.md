# TODO

## Done
- [x] MVVM architecture (Model → Repository → ViewModel → Compose)
- [x] JWT login via PostgREST (policemen table + login RPC function)
- [x] Search by license or plate (returns driver + vehicle + history)
- [x] Create Procès-Verbal (infraction + officer_id + location)
- [x] Offline sync (Room cache + SyncQueue, server wins on conflict)
- [x] Room database (drivers, vehicles, infractions, sync_queue)
- [x] Navigation Compose (Login → Search → PV screens)
- [x] DataStore for JWT token persistence
- [x] PostgreSQL schema with policemen table + JWT function

## Next Steps
- [ ] Deploy PostgreSQL + PostgREST on Docker
- [ ] Test JWT login flow end-to-end
- [ ] Pass officerId from LoginViewModel to PVViewModel properly
- [ ] Add pull-to-refresh on SearchScreen
- [ ] Add infraction type dropdown (common types)
- [ ] Add date picker instead of text input
- [ ] Add location autocomplete / GPS
- [ ] Add PV export (PDF generation)
- [ ] Add biometric unlock after first login
- [ ] Add push notifications for new infractions
- [ ] Unit tests for ViewModels + Repositories
- [ ] UI tests for critical flows
