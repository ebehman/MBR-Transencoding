package com.MBR.transcoder.MBR.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider

public class MissingFileException  extends Throwable implements ExceptionMapper<MissingFileException>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MissingFileException() {
        super("No File found with given name !!, Check the Preset ");
    }
 
    public MissingFileException(String string) {
        super(string);
    }
	@Override
	public Response toResponse(MissingFileException exception) {
		return Response.status(500).entity(exception.getMessage())
                .type("text/plain").build();
	}

}
