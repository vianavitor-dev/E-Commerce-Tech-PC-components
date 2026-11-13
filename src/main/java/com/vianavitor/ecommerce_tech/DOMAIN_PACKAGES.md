# Domain packages

This project uses a pragmatic, package-by-domain form of DDD. Spring services coordinate use cases; JPA entities and Spring Data repositories stay inside their owning domain. No extra interfaces or duplicate persistence models are required.

- `auth`: accounts, roles, login, tokens, security configuration and authenticated principals.
- `ecommerce`: product catalog, hardware compatibility, customer ratings, orders and purchased items. Controllers, services, entities, repositories and DTOs are local to this domain.
- `payment`: reserved for payment processing and provider integration. Order cancellation/refund status currently remains in ecommerce; there is no payment gateway implementation yet.
- `invetory`: reserved for stock levels and reservations (existing folder spelling retained). Catalog hardware specifications belong to ecommerce; stock tracking is not implemented yet.
- `shared`: resource-not-found errors, global HTTP exception translation and the reusable read-only repository contract.

Keep domain-specific DTOs, exceptions, enums and JPA converters with their owner. Cross-domain calls should use application services where practical. Existing JPA associations across domains are retained for simplicity. HTTP routes, database mappings and business behavior are unchanged.
