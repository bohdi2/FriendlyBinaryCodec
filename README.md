FriendlyBinaryCodec
===================

I have often had to work with binary messages with strange formats. Typically these are in legacy systems
and are originally based on C structures. Creating codecs for these messages is error prone.

The FriendlyBinaryCodec is a general purpose byte level codec that assists in testing such messages. FBC is
not meant to be performant but to be, well to be friendly. You can pass it a raw binary message and it
will try to decode the message according to the rules you've defined and it will print a trace of the decoded
fields along with their names.
