# Kotlin Rewrite Diff Notes

The original project is a Java client that spans multiple API domains (chat, moderation, livestreams, users, public keys, events, etc.)
across `src/main/java/com/mbayou/kick4k` (the legacy Kick4J codebase). Those sources alone add up to a little over 3,100 lines of code, and the accompanying JUnit
suite in `src/test/java` contributes another ~770 lines.

When we rebuilt the library around new Kotlin stubs, we removed that entire Java tree as well as the Java tests. The Kotlin replacements
consisted of a handful of models, an authorization helper, and a skeletal client/dispatcher—roughly 240 lines in `src/main/kotlin` plus a
28-line smoke test under `src/test/kotlin`.

That is why the diff for the Kotlin rewrite shows several thousand deletions paired with only a few hundred additions: the full Java
surface (clients, data transfer objects, helpers, and tests) was deleted, while only a minimal Kotlin facade was added. Functionally, the
Kotlin-only snapshot could not exercise the majority of Kick endpoints because those Java classes no longer existed.
