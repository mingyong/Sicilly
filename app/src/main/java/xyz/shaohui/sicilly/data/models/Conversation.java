package xyz.shaohui.sicilly.data.models;

/**
 * Created by shaohui on 16/8/20.
 */
public class Conversation extends ConversationBean {

    private Message dm;
    private String otherid;
    private boolean new_conv;

    public Conversation(int count) {
        super(count);
    }

    public Message getDm() {
        return dm;
    }

    public void setDm(Message dm) {
        this.dm = dm;
    }

    public String getOtherid() {
        return otherid;
    }

    public void setOtherid(String otherid) {
        this.otherid = otherid;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public boolean isNew_conv() {
        return new_conv;
    }

    public void setNew_conv(boolean new_conv) {
        this.new_conv = new_conv;
    }
}
