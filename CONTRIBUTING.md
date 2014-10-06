# Contributing guideline

Any contributions are welcome!

## IDE

Please use Android Studio(0.8.6) and format your codes.

## Tests

Check if all the tests pass on a device.

```sh
$ ./gradlew clean :simplealertdialog:connectedCheck
```

## Coverage

Check test coverage after executing tests.

```sh
(If you are a Mac OS X user:)
$ open simplealertdialog/build/outputs/reports/coverage/debug/index.html
```

## Documentation

If you add or modify Java elements, please update comments
and check them with the following commands.

```sh
$ ./gradlew androidJavadoc
(If you are a Mac OS X user:)
$ open simplealertdialog/build/outputs/docs/javadoc/index.html
```
