# Replicant with atom based state management

This is a very brief example of how to build UIs with Replicant and atom based
state management.

The [core namespace](./src/state_atom/core.cljs) wires up a mini-framework where
you can implement actions. The [ui namespace](./src/state_atom/ui.cljc) contains
pure functions to render the UI.

The development build is initialized from [the dev
namespace](./dev/state_atom/dev.cljs). There is also a [prod
namespace](./src/state_atom/prod.cljs) which could be used as the target for a
production build.

There is a router setup here as well. Check out the [state-setup
branch](https://github.com/cjohansen/replicant-state-atom/tree/state-setup) for
the version that only has state management.
