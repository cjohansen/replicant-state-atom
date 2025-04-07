# Replicant with atom based state management

This is a very brief example of how to build UIs with atom based state
management with Replicant and Nexus.

The [core namespace](./src/state_atom/core.cljs) wires up a mini-framework where
you can implement actions and effects. The [ui
namespace](./src/state_atom/ui.cljc) contains pure functions to render the UI.

The development build is initialized from [the dev
namespace](./dev/state_atom/dev.cljs). It sets up data visualization with
[Dataspex](https://github.com/cjohansen/dataspex). There is also a [prod
namespace](./src/state_atom/prod.cljs) which could be used as the target for a
production build.

There is a router setup here as well. Check out the [state-setup
branch](https://github.com/cjohansen/replicant-state-atom/tree/state-setup) for
the version that only has state management.

## License: MIT

Copyright © 2025 Christian Johansen. Distributed under the [MIT
License](https://opensource.org/license/mit).
