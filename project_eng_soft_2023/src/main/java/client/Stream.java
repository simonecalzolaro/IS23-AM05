package client;

import myShelfieException.InvalidOperationException;
import myShelfieException.InvalidParametersException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Stream {

    Socket socket;
    int code;

    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     *
     * @param socket socket used to build the stream
     * @param code indicates the direction of the stream
     *             ==> code = 1: input
     *             ==> code = 0: output
     * @throws IOException
     * @throws InvalidParametersException
     */

    public Stream(Socket socket, int code) throws IOException, InvalidParametersException {

        this.socket = socket;
        this.code = code;

        initializeStream();

    }


    /**
     * Initialize the stream as a output/input stream
     * output ==> code = 0
     * input ==> code = 1
     *
     * @throws IOException method unable to create the stream
     * @throws InvalidParametersException users gave a wrong code to the method
     */

    private void initializeStream() throws IOException, InvalidParametersException {

        switch (code){

            case 0:
                out = new ObjectOutputStream(socket.getOutputStream());
                break;

            case 1:
                in = new ObjectInputStream(socket.getInputStream());
                break;

            default:
                throw new InvalidParametersException();

        }

    }

    /**
     *
     * @param jo receive from the user the JSONObject that he wants to flush
     * @throws IOException thrown when the stream it's not able to flush the object
     * @throws InvalidOperationException thrown when the stream had been built has an input stream ,i.e, code = 1
     */

    public void write(JSONObject jo) throws IOException, InvalidOperationException {

        if(code == 0){
            out.writeObject(jo);
            out.flush();
        }
        else throw new InvalidOperationException();
    }


    /**
     * Read the object coming from the network and return it to the users
     * @return JSONObject from the network
     * @throws IOException thrown if the method is not able to read the object from the network
     * @throws ClassNotFoundException
     * @throws InvalidOperationException thrown when the stream had been built has an output stream ,i.e, code = 0
     */

    public JSONObject read() throws IOException, ClassNotFoundException, InvalidOperationException {


        if(code == 1) {
            JSONObject jo = new JSONObject();
            jo = (JSONObject) in.readObject();
            return jo;
        }
        else throw new InvalidOperationException();

    }


    /**
     * Reset the stream
     *
     * @throws IOException
     * @throws InvalidOperationException thrown when the stream had been built has an input stream ,i.e, code = 1
     */

    public void reset() throws IOException, InvalidOperationException {

        if(code == 0) out.reset();
        else throw new InvalidOperationException();

    }




}
