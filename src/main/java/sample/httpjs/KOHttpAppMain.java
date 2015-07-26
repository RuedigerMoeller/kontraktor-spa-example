package sample.httpjs;

import org.nustaq.kontraktor.remoting.encoding.SerializerType;
import org.nustaq.kontraktor.remoting.http.Http4K;

import java.io.File;
import java.io.IOException;

import static org.nustaq.kontraktor.Actors.AsActor;

/**
 * Created by ruedi on 04/06/15.
 *
 * Just setup and start server
 *
 */
public class KOHttpAppMain {

    public static void main(String[] args) throws IOException {
        // just setup stuff manually here. Its easy to build an application specific
        // config using e.g. json or kson.
        File root = new File("./web");

        if ( ! new File(root,"index.html").exists() ) {
            System.out.println("Please run with working dir: '[..]/spa-knockout");
            System.exit(-1);
        }

        // create server actor
        KOHttpApp app = AsActor(KOHttpApp.class);

        Http4K.Build("localhost", 8080)
            .resourcePath("/")
                .elements(
                    "./web",
                    "./weblib"
                )
                .allDev(true)
//                .minify(false) // in case of debugging import shim
                .cacheAggregates(false)
                .build()
            .httpAPI("/api", app)
                .serType(SerializerType.JsonNoRef)
                .setSessionTimeout(30_000)
                .build()
            .websocket("ws", app)
                .serType(SerializerType.JsonNoRef)
                .build()
            .build();
    }

}
