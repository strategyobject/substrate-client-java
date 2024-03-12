{
  description = "Environment flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };

        mkLinkerPath = {cc, ...}:
            # let
              # inherit (stdenv) cc;
            # in
              "${cc}/bin/${cc.targetPrefix}cc";

      in
        {
          devShells.default = with pkgs; mkShell {

            CARGO_TARGET_AARCH64_UNKNOWN_LINUX_GNU_LINKER = mkLinkerPath pkgsCross.aarch64-multiplatform.stdenv;
            CARGO_TARGET_X86_64_UNKNOWN_LINUX_GNU_LINKER = mkLinkerPath pkgsCross.gnu64.stdenv;
            CARGO_TARGET_X86_64_PC_WINDOWS_GNU_LINKER = mkLinkerPath pkgsCross.mingwW64.stdenv;

            # for mingw compilation
            PTHREAD_LOCATION = "${pkgs.pkgsCross.mingwW64.windows.pthreads}/lib";

            shellHook = "rustup show";

            packages = [
              temurin-bin-8
              rustup
            ];
          };
        }
    );
}
