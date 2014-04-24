# jsense

jsense is a Java library to work with sensing. Because sensing is nice.

[![Build Status](https://travis-ci.org/markuswustenberg/jsense.png?branch=master)](https://travis-ci.org/markuswustenberg/jsense)
[![Gitter chat](https://badges.gitter.im/markuswustenberg/jsense.png)](https://gitter.im/markuswustenberg/jsense)
[![Coverage Status](https://coveralls.io/repos/markuswustenberg/jsense/badge.png?branch=master)](https://coveralls.io/r/markuswustenberg/jsense?branch=master)

## Project goals

The goal of jsense is to provide libraries that support common uses of sensing data. Sensing data can come from sensors such as accelerometers, gyroscopes, magnetometers (compasses), GPS information, temperature sensors, etc. Common uses are:

- Basic data handling: Providing a common model for handling sensor data across platforms, saving and loading data in various formats, converting between formats.
- Data processing: Preparing for activity recognition (with feature selection and computation, machine learning, gesture recognition).
- Generally, to make common tasks easy to undertake.
- Provide all of this under [a very liberal open source license](https://github.com/markuswustenberg/jsense/blob/master/LICENSE) (the MIT license).

## Project values

- Providing code with a clean and nice API that is easy and intuitive to use makes for better programs with less bugs.
- Favouring immutable classes (see Effective Java item 15) makes concurrency easier.
- Tested code is good code, and defensive programming reduces bugs.
- Minimising loading data into memory, instead working online in a streaming fashion, is good and sometimes necessary for large datasets.

## Structure

jsense has several components:

- `jsense-core` contains the core classes, such as the model (`AccelerometerEvent` etc.), the `Serializer` and `Deserializer` interfaces and basic implementations of these, as well as classes for basic data processing.
- `jsense-json` contains classes for converting the model classes to/from JSON.
- `jsense-protobuf` is for converting the model classes to/from [Google Protocol Buffers](https://developers.google.com/protocol-buffers/) format.
- `jsense-tools` will [soon](https://github.com/markuswustenberg/jsense/issues/26) contain convenience tools for handling data.

## Dependencies

jsense depends on a few libraries, depending on which components are loaded. These are always used in jsense:

- [Google Guava](https://code.google.com/p/guava-libraries/): A really well-designed library for all kinds of common code, as well as some nice collection classes.
- [Joda-Time](http://www.joda.org/joda-time/): Because working with time is notoriously hard, and this library makes it much easier. Time is inherent in sensing data, so it makes sense to include a library like this.

For `jsense-json` respectively `jsense-protobuf` only:

- [Gson](https://code.google.com/p/google-gson/): A nice little and easy-to-use JSON serialization/deserialization library.
- [Google Protocol Buffers](https://developers.google.com/protocol-buffers/): Converting from/to a very efficient binary format.
