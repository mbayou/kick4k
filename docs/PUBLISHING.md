# Publishing Kick4k

Kick4k is released through [JitPack](https://jitpack.io/#mbayou/kick4k). Rather than pushing artifacts to Maven
Central, JitPack builds straight from tagged commits in this repository and serves them as
`com.github.mbayou:kick4k:<version>`. Use the following checklist whenever you want to cut a new version.

## 1. Prepare the release

1. **Pick the version**
   - Update the `version` in [`build.gradle.kts`](../build.gradle.kts) and any docs/snippets if the API changed.
   - Commit the change.
2. **Run the tests**
   - `./gradlew clean build --console=plain` must succeed on a clean checkout.
3. **Tag the commit**
   - `git tag v1.2.0 && git push origin v1.2.0` (replace with the version you chose).

## 2. Trigger the JitPack build

1. Navigate to [https://jitpack.io/#mbayou/kick4k](https://jitpack.io/#mbayou/kick4k).
2. Select the new tag and click **Get it**.
3. Wait for the build log to show `BUILD SUCCESS`. JitPack runs `./gradlew` using Java 21 (configured via
   [`jitpack.yml`](../jitpack.yml)).

Once complete, the artifact is available at:

```
implementation("com.github.mbayou:kick4k:1.2.0")
```

If you need to rebuild a tag (e.g., after fixing the build), use the **Rebuild** button on JitPack. For consumers who
need snapshots, they can depend on branches or commit hashes (`com.github.mbayou:kick4k:-SNAPSHOT`).

## 3. Update documentation

- Update the README badge or installation instructions if the version changes.
- Optionally create a GitHub Release pointing to the tag with release notes.

That’s it—no OSSRH credentials or Sonatype staging steps are required anymore.
