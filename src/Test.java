import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;


public class Test {
	public static void main(String[] args) throws CharacterCodingException {
		String some_string = "This is a string that Java natively stores as Unicode.";
		 Charset latin1_charset = Charset.forName("ISO-8859-1");
		 
		 ByteBuffer latin1_bbuf = latin1_charset.newEncoder().encode(CharBuffer.wrap(some_string));
		 byte[] bs = latin1_bbuf.array();
		 System.out.println(new String(bs));
	}
}
