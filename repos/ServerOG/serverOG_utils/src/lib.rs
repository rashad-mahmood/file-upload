
pub mod comm {

    extern crate bytes;
    extern crate futures;
    extern crate tokio_io;
    extern crate tokio_proto;
    extern crate tokio_service;
    use std::io;
    use std::str;
    use self::bytes::BytesMut;
    use self::tokio_io::codec::{Encoder, Decoder};
    use self::tokio_proto::pipeline::ServerProto;
    use self::tokio_io::{AsyncRead, AsyncWrite};
    use self::tokio_io::codec::Framed;
    use self::tokio_service::Service;
    use self::futures::{future, Future, BoxFuture};
    pub struct LineCodec;

    impl Decoder for LineCodec {
        type Item = String;
        type Error = io::Error;

        fn decode(&mut self, src: &mut BytesMut) -> io::Result<Option<String>> {
            if let Some(i) = src.iter().position(|&b| b == b'\n') {
                let line = src.split_to(i);
                src.split_to(1);

                match str::from_utf8(&line) {
                    Ok(s) => Ok(Some(s.to_string())),
                    Err(_) => Err(io::Error::new(io::ErrorKind::Other,"invalid UTF-8")),
                }
            } else {
                Ok(None)
            }
        }

    }

    impl Encoder for LineCodec {
        type Item = String;
        type Error = io::Error;

        fn encode(&mut self, msg: String, buf: &mut BytesMut) -> io::Result<()> {
            buf.extend(msg.as_bytes());
            buf.extend(b"\n");
            Ok(())
        }
    }

    pub struct LineProto;

    impl<T: AsyncRead + AsyncWrite + 'static> ServerProto<T> for LineProto {
        type Request = String;
        type Response = String;

        type Transport = Framed<T, LineCodec>;
        type BindTransport = Result<Self::Transport, io::Error>;
        fn bind_transport(&self, io: T) -> Self::BindTransport {
            Ok(io.framed(LineCodec))
        }
    }

    pub struct Echo;

    impl Service for Echo {
        type Request = String;
        type Response = String;
        type Error = io::Error;
        type Future = BoxFuture<Self::Response, Self::Error>;

        fn call(&self, req: Self::Request) -> Self::Future {
            future::ok(req).boxed()
        }
    }


    #[test]
    fn it_works() {
    }
}
