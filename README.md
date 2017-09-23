# SecureApp

[![MIT license](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)

**Telematics Engineering Final Project**

This is the Java Core for my final degree project.

## Usage

- **Slice & Encrypt**: Takes a file path and a block size. Then, it slices the file and encrypts each slice with AES 128 key algorythm in CBC mode. Each slice is signed with RSASSA-PSS algorythm. The symmetric key is also signed and encrypted using RSA with a 4096 bit key.

- **Decrypt & Compose**: Takes the folder path where the encrypted slices and the key are. It verifies the signature of the key and then decrypts the files, verifies all signatures and, if there has not been any problem, composes the original file.
