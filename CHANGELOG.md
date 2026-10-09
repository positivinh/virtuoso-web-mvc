# Changelog

All notable changes to this repository are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- `web-mvc-starter`: RFC 7807 `ProblemDetail` error responses built by `ProblemDetailFactory` (allow-listed context
  keys, `errors` without rejected values, `WARN` for 4xx), and a fallback advice mapping any `ApplicationException` to
  `400`.
- Dummy project and reusable CI workflows.

[Unreleased]: https://github.com/positivinh/virtuoso-web-mvc/commits/main
