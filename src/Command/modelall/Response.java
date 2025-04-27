package Command.modelall;

import java.io.Serializable;

public class Response implements Serializable {
    private final boolean ok;
    private final String  text;
    private final Object  data;

    private Response(boolean ok, String text, Object data) {
        this.ok = ok; this.text = text; this.data = data;
    }

    public static Response ok(String text) {
        return new Response(true , text, null ); }
    public static Response ok(String text, Object data){
        return new Response(true , text, data ); }
    public static Response error(String text){
        return new Response(false, text, null ); }

    public boolean isOk()  { return ok;   }
    public String  getText() { return text; }
    public Object  getData() { return data; }
}
