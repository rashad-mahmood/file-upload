extern crate threadpool;
extern crate tokio_proto;
extern crate serverOG_utils;

use std::io::{self, BufReader};
use std::io::prelude::*;
use std::net::TcpListener;
use std::net::TcpStream;
use threadpool::ThreadPool;
use std::time::Instant;
use tokio_proto::TcpServer;

fn main () {
    // Specify the localhost address
    let addr = "127.0.0.1:8080".parse().unwrap();

    // The builder requires a protocol and an address
    let server = TcpServer::new(serverOG_utils::comm::LineProto, addr);

    // We provide a way to *instantiate* the service for each new
    // connection; here, we just immediately return a new instance.
    server.serve(|| Ok(serverOG_utils::comm::Echo));
}
