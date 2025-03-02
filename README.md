# Spring Boot Diagnostic
⚠️ Unsafe for production use ⚠️

A small  [spring-boot-admin](https://github.com/codecentric/spring-boot-admin) docker image for operations debugging
and diagnostic.

Includes
- spring-boot-admin server that provides information about itself, mainly to check config setup.
- basic shell utility (sh, curl, ip-tools, ...) to investigate communication

Purposes
- test instance
- operations debugging during basic setup