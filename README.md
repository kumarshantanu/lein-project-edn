# lein-project-edn

Leiningen plugin to emit project details as [EDN](https://github.com/edn-format/edn).


## Usage

Install as a project-level plugin by editing `project.clj` as follows:

```clojure
  :plugins [[lein-project-edn "0.2.0"]]
  ;; plugin config is optional, provide only what you need
  :project-edn {:output-file "resources/project.edn"  ; file name to output EDN (default: STDOUT)
                :output-prefix ";; Auto-generated\n"  ; string prefix for the generated EDN output (default: empty)
                :output-suffix ";; End of auto-gen\n" ; string suffix for the generated EDN output (default: empty)
                :select-keys [:dependencies :version] ; keys to select from project map (default: all keys)
                :remove-keys [:test-selectors]        ; keys for removal (default: [:injections :uberjar-merge-with])
                :verify-edn? true                     ; whether verify EDN by parsing (default: true)
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
