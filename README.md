# http4s-scala3

Java 22, Scala 3, http4s, and sbt project template.

To start the local database (from the project root directory):

```
. ./run-local.sh
```

The script supports the standard docker compose commands (up, down, start, stop). To get help:

```
. ./run-local.sh help
```

This requires docker to be installed on your machine; [docker desktop](https://www.docker.com/get-started/) is a good
place to start.

Once the database is running, in the sbt shell, run `~reStart` command to run a hot reloading server. This will
recompile on code changes. Save the file again to prompt another recompile.
