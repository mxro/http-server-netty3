/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3;

import java.io.ByteArrayOutputStream;

import org.jboss.netty.channel.MessageEvent;

public interface ByteStreamHandler {

    public void processRequest(ByteArrayOutputStream receivedData,
            MessageEvent e);


}
