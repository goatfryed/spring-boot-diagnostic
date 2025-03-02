#!/usr/bin/env sh
set -e

if [ "$#" -eq 0 ]; then
    echo "Usage: $0 <package1> <package2> ..." >&2
    exit 1
fi
packages="$@"

echo "Adding packages: $(echo $packages | tr -s ' ' | tr '\n' ' ')" >&2

apt-get update

all_dependencies=$(echo $packages \
  | xargs -n1 apt depends --recurse --no-suggests --no-conflicts --no-breaks --no-replaces --no-enhances --no-recommends \
  | grep -E "^ +Depends:" \
  | awk '{print $2}' \
  | sort -u)
package_dependencies=$(echo "$all_dependencies" | grep -vE "^<.*>$")
virtual_dependencies=$(echo "$all_dependencies" | grep -E "^<.*>$" | tr -d '<>')

echo "Also adding dependencies:" >&2
echo "$package_dependencies" | tr -s ' ' | tr '\n' ' ' | fold -s -w 120 >&2 | sed 's/^/    /' >&2
echo "\n"
if [ -n "$virtual_dependencies" ]; then
  echo "Ensure the following virtual dependencies are provided:" >&2
  echo "$virtual_dependencies\n" | tr -s ' ' | tr '\n' ' ' | fold -s -w 120 >&2 | sed 's/^/    /' >&2
  echo "\n"
fi

mkdir -p /tmp/deb_downloads
mkdir -p /run-image
cd /tmp/deb_downloads

apt-get download $packages $package_dependencies

for pkg in /tmp/deb_downloads/*.deb; do
    dpkg-deb -x "$pkg" /run-image;
done

apt-get clean
rm -rf /tmp/deb_downloads
rm -rf /var/lib/apt/lists/*
