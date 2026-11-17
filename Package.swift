// swift-tools-version:5.9

import PackageDescription

let UsercentricsVersion: Version = "2.35.0"

let package = Package(
    name: "cordova-plugin-usercentrics",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "cordova-plugin-usercentrics",
            targets: ["cordova-plugin-usercentrics"]
        )
    ],
    dependencies: [
        .package(url: "https://github.com/apache/cordova-ios.git", branch: "master"),
        .package(url: "https://bitbucket.org/usercentricscode/usercentrics-spm-sdk", exact: UsercentricsVersion),
        .package(url: "https://bitbucket.org/usercentricscode/usercentrics-spm-ui.git", exact: UsercentricsVersion)
    ],
    targets: [
        .target(
            name: "cordova-plugin-usercentrics",
            dependencies: [
                .product(name: "Cordova", package: "cordova-ios"),
                .product(name: "Usercentrics", package: "usercentrics-spm-sdk"),
                .product(name: "UsercentricsUI", package: "usercentrics-spm-ui")
            ],
            path: "src/ios",
            publicHeadersPath: "."
        )
    ]
)