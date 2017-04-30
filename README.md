# lein-project-edn

Leiningen plugin to emit project details as [EDN](https://github.com/edn-format/edn).


## Usage

Install as a project-level plugin by editing `project.clj` as follows:

```clojure
  :plugins [[lein-project-edn "0.1.0-SNAPSHOT"]]
  :project-edn {:output-file "resources/project.edn"  ; file name to output EDN (optional, default: STDOUT)
                :verify-edn? true                     ; whether verify EDN by parsing (optional, default: true)
                }
  :hooks [leiningen.project-edn/activate]  ; optional, when enabled auto-triggers on compile (i.e. test/jar etc)
```

The above example outputs project details into `resources/project.edn` with each of the following commands:

```
$ lein test
$ lein jar
$ lein uberjar
```

When the `:hooks` entry is not specified, you must explicitly execute the plugin as follows:

```bash
$ lein project-edn
```


## License

Copyright © 2017 Shantanu Kumar (kumar.shantanu@gmail.com)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
