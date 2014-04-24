# jsense

A Java library to work with sensing. Because sensing is nice.

[![Build Status](https://travis-ci.org/markuswustenberg/jsense.png?branch=master)](https://travis-ci.org/markuswustenberg/jsense)
[![Gitter chat](https://badges.gitter.im/markuswustenberg/jsense.png)](https://gitter.im/markuswustenberg/jsense)

## Project goals

The goal of jsense is to provide libraries that support common uses of sensing data. Sensing data can come from sensors such as accelerometers, gyroscopes, magnetometers (compasses), GPS information, temperature sensors, etc. Common uses are:

- Basic data handling: Providing a common model for handling sensor data across platforms, saving and loading data in various formats, converting between formats.
- Data processing: Preparing for activity recognition (with feature selection and computation, machine learning, gesture recognition).
- Generally, to make common tasks easy to undertake.

## Project values

- Providing code with a clean and nice API that is easy and intuitive to use.
- Favour immutable classes (see Effective Java item 15).
- Tested code is good code.
- Minimise loading data into memory, prefer instead to work online in a streaming fashion.

## Structure

jsense has several components:

- `jsense-core` contains the core classes, such as the model (`AccelerometerEvent` etc.), the `Serializer` and `Deserializer` interfaces and basic implementations of these, as well as classes for basic data processing.
- `jsense-json` contains classes for converting the model classes to/from JSON.
- `jsense-protobuf` is for converting the model classes to/from [Google Protocol Buffers](https://developers.google.com/protocol-buffers/) format.
- `jsense-tools` will [soon](https://github.com/markuswustenberg/jsense/issues/26) contain convenience tools for handling data.

