#!/bin/bash
./gradlew assembleDebug
./gradlew -q moveDebugVersionFile

./gradlew assembleRelease
./gradlew -q moveReleaseVersionFile
