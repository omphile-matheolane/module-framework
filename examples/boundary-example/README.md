# Module boundary example

Requires Java 17+ and Maven 3.

Run `mvn test` in `before`: the architecture test must fail.
Run `mvn test` in `after`: the same test must pass.

Only the ordering service dependency changes: PaymentGatewayImpl becomes PaymentGateway.
The payment implementation is deliberately empty; this example checks dependency structure.

Example code: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0).
