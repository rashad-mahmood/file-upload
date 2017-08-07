use std::io;
use std::io::prelude::*;
use std::net::TcpStream;

fn main() {
    let addr = "127.0.0.1:8080".to_string();


    let mut stream = TcpStream::connect(addr).unwrap();
    'input: loop {
        let mut user_input = String::new();
        let _ = io::stdin().read_line(&mut user_input);

        match user_input.as_ref() {
            "exit\r\n" => {
                break 'input;
            }
            _ => {
                let _ = stream.write(user_input.as_bytes());
                let _ = stream.flush();
            }
        }
    }
}
