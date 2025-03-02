# Builder stage: Install dependencies into /run-image
FROM paketobuildpacks/run-jammy-base:latest as builder
USER root

# Update and install dependencies into a separate directory (/run-image)
# installer script
COPY install_packages.sh /usr/local/bin/install_packages.sh

# essentials
RUN /usr/local/bin/install_packages.sh musl busybox
# a lot of packages want debconf, which is only required in config assist.
# we assume, it's not relevant and leave it out

# utils
RUN /usr/local/bin/install_packages.sh curl wget
RUN /usr/local/bin/install_packages.sh iputils-ping net-tools dnsutils iproute2

# patches for more missing dependencies added here for better incremental build
# none

# must be absolute in terms of run image
# RUN ln -sf /bin/busybox /run-image/bin/sh

FROM paketobuildpacks/run-jammy-tiny:latest

COPY --from=builder /run-image /
SHELL ["/bin/busybox", "sh", "-c"]

USER root
RUN busybox --install -s /bin
USER cnb