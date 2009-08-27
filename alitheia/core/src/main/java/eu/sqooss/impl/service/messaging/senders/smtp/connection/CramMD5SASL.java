/*
 * This file is part of the Alitheia system, developed by the SQO-OSS
 * consortium as part of the IST FP6 SQO-OSS project, number 033331.
 *
 * Copyright 2007-2008 by the SQO-OSS consortium members <info@sqo-oss.eu>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package eu.sqooss.impl.service.messaging.senders.smtp.connection;

import java.util.Properties;

import eu.sqooss.impl.service.messaging.senders.smtp.utils.Base64;

public class CramMD5SASL implements SASL {

    public String getID() {
        return "CRAM-MD5";
    }

    public String getResponse(Properties props, String serverResponse) {
        if(serverResponse.equals("")||props.getProperty(Constants.USER)==null||props.getProperty(Constants.PASS)==null){
            return null;
        }
        byte[] key = props.getProperty(Constants.PASS).getBytes();
        byte[] text=null;
        try {
            text = Base64.decode(serverResponse.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
//      System.out.println("REsponse for MD5 : "+text);
        MD5 md=new MD5();
        if(key.length>64){
            md.update(key);
            key=md.digest();
        }
        key=addZeros(key);
        byte[] ikey = pad(key,0x36);
        byte[] okey = pad(key,0x5C);
        md=new MD5();
        md.update(ikey);
        md.update(text);
        byte[] temp = md.digest();
        md=new MD5();
        md.update(okey);
        md.update(temp);
        temp = md.digest();
        return hexprint(temp,props.getProperty(Constants.USER));
        /*    byte[] user = props.getProperty(Constants.USER).getBytes();
    byte[] fin = new byte[user.length+1+temp.length];
    System.arraycopy(user, 0, fin, 0, user.length);
    fin[user.length]=0x20;
    System.arraycopy(temp, 0, fin, user.length+1, temp.length);
    return new String(Base64.encode(fin));
         */  }

    private String hexprint(byte[] temp, String user) {
        StringBuffer res = new StringBuffer(user);
        res.append(' ');
        String tmp="";
        for(int i=0;i<temp.length;i++){
            tmp=Integer.toHexString(temp[i]&0xff);
            if(tmp.length()==1){
                res.append('0');
            }
            res.append(tmp);
        }
        return new String(Base64.encode(res.toString().getBytes()));
    }

    private byte[] pad(byte[] key, int b) {
        byte[] nw = new byte[key.length];
        for(int i=0;i<nw.length;i++){
            nw[i]=(byte)(key[i] ^ b);
        }
        return nw;
    }

    private byte[] addZeros(byte[] key) {
        byte[] nw = new byte[64];
        System.arraycopy(key, 0, nw, 0, key.length);
        for(int i=key.length;i<nw.length;i++){
            nw[i]=0;
        }
        return nw;
    }

//  public static void main(String[] args){
//  Properties props = new Properties();
//  props.setProperty(Constants.USER, "tim");
//  props.setProperty(Constants.PASS, "tanstaaftanstaaf");
//  String s = new CramMD5SASL().getResponse(props, new String(Base64.encode("<1896.697170952@postoffice.reston.mci.net>".getBytes())));
//  System.out.println("CramMD5SASL.main():"+s);
//  byte[] res = new byte[]{0x75, 0x0c, (byte)0x78, (byte)0x3e, (byte)0x6a, (byte)0xb0, (byte)0xb5, (byte)0x03, (byte)0xea, (byte)0xa8, (byte)0x6e, (byte)0x31, (byte)0x0a, (byte)0x5d, (byte)0xb7, (byte)0x38};
//  System.out.println("CramMD5SASL.main():"+new String(Base64.encode(res)));
//  }

}

//vi: ai nosi sw=4 ts=4 expandtab