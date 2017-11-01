package org.dlaws.utils;

import java.io.IOException;
import java.io.OutputStream;

public class Utils
{
    public static Byte[] boxBytes( byte[] sourceBytes, int offset, int length )
    {
        int sourceLength = sourceBytes.length;

        if ( sourceLength - offset < length )
        {
            throw new RuntimeException("Can not copy " + length + " byte starting from offset " + offset +
                    " from source with " + sourceLength + " bytes.");
        }

        Byte[] boxedBytes = new Byte[ length ];

        int i = offset;

        for ( byte b : sourceBytes )
        {
            boxedBytes[i++] = b;
        }

        return boxedBytes;
    }

    public static byte[] unboxBytes( Byte[] sourceBytes, int offset, int length )
    {
        int sourceLength = sourceBytes.length;

        if ( sourceLength - offset < length )
        {
            throw new RuntimeException("Can not copy " + length + " byte starting from offset " + offset +
                    " from source with " + sourceLength + " bytes.");
        }
        byte[] primitiveBytes = new byte[ length ];

        int i = offset;

        for ( byte b : sourceBytes )
        {
            primitiveBytes[i++] = b;
        }

        return primitiveBytes;
    }

    public static void writeBytes( OutputStream output, Byte[] sourceBytes, int offset, int length )
            throws IOException
    {
        byte[] bytes = unboxBytes( sourceBytes, offset, length );

        output.write( bytes );
    }

    public static void writeBytes( OutputStream output, byte[] bytes, int offset, int length )
            throws IOException
    {
        output.write(bytes,offset,length);
    }

}
