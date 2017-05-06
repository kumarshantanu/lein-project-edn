# TODO & Change Log

## TODO

None


## 0.2.0 / 2017-May-07

- Plugin options
  - `:select-keys`   - only specified keys are selected from project map
  - `:remove-keys`   - specified keys are removed, default: `[:injections :uberjar-merge-with]`
  - `:output-prefix` - string prefix for the generated EDN output
  - `:output-suffix` - string suffix for the generated EDN output


## 0.1.0 / 2017-May-01
### Added
- Emit project details as EDN
- Plugin options
  - Output filename: `:output-file`
  - Verify EDN text: `:verify-edn?`
- Leiningen hook to attach to compile
- Related Leiningen issue: https://github.com/technomancy/leiningen/issues/2221
