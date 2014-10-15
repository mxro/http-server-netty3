package de.mxro.httpserver.netty3.internal;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class IdleHandler extends IdleStateAwareChannelHandler {
    @Override
    public void channelIdle(final ChannelHandlerContext ctx, final IdleStateEvent e) {
        if (e.getState() == IdleState.READER_IDLE) {
            ctx.close();
            e.getChannel().close();
        } else if (e.getState() == IdleState.WRITER_IDLE) {
            e.getChannel().write("dummy");
        }
    }
}
