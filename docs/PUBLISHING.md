# Publishing Kick4k

This project already includes Gradle's `maven-publish` plugin and is configured to push artifacts to the TekSUSik Reposilite instance under the `com.mbayou` group. Use the following checklist whenever you need to publish a new build of the library.

## 1. Prerequisites

1. **Choose the right version**
   - Keep `version` in [`build.gradle.kts`](../build.gradle.kts) suffixed with `-SNAPSHOT` while iterating.
   - Before a public release, drop the `-SNAPSHOT` suffix and commit the change.
2. **Set credentials**
   - Configure your username/token either in `~/.gradle/gradle.properties`:
     ```properties
     reposiliteUser=<username>
     reposiliteToken=<token>
     ```
   - or export them via environment variables `REPOSILITE_USER` / `REPOSILITE_TOKEN`.
3. **Verify tests**
   - Run `./gradlew clean test --console=plain --no-daemon` and make sure the suite passes before pushing artifacts.

## 2. Publishing snapshots

Snapshots automatically go to `https://repo.teksusik.pl/snapshots` because the Gradle script derives the repository from the version suffix. Publish with:

```bash
./gradlew publish --console=plain --no-daemon
```

## 3. Publishing releases

1. Update `version` in `build.gradle.kts` to the desired semantic version _without_ `-SNAPSHOT`.
2. Commit and tag the release commit (e.g., `git tag v1.1.0`).
3. Run the publish task:
   ```bash
   ./gradlew clean publish --console=plain --no-daemon
   ```
4. Create the release in your Git hosting provider referencing the tag.

Gradle will automatically select `https://repo.teksusik.pl/releases` for non-snapshot versions.

## 4. Maven Central / other hosts

If you later decide to push Kick4k to a different repository such as Maven Central:

1. Add the appropriate `maven { ... }` block inside the `publishing.repositories` section in `build.gradle.kts`.
2. Provide the necessary credentials/signing configuration in your Gradle properties.
3. Re-run `./gradlew publish` targeting the new repository name, e.g. `./gradlew publishAllPublicationsToMavenCentralRepository`.

Documenting the release steps in your project wiki or CI pipeline will make the process reproducible for new maintainers.
