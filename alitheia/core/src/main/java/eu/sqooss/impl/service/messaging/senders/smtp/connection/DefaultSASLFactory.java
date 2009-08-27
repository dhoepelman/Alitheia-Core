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

import java.util.Enumeration;
import java.util.Hashtable;

public class DefaultSASLFactory implements SASLFactory {

    Hashtable < String, SASL > recognizedSecurityMethods =
        new Hashtable < String, SASL >();

    public DefaultSASLFactory(){
        SASL tmp = new CramMD5SASL();
        recognizedSecurityMethods.put(tmp.getID(),tmp);
        tmp = new LoginSASL();
        recognizedSecurityMethods.put(tmp.getID(),tmp);
        tmp = new PlainSASL();
        recognizedSecurityMethods.put(tmp.getID(),tmp);
        tmp = new AnonymousSASL();
        recognizedSecurityMethods.put(tmp.getID(),tmp);
    }


    public SASL[] getAvailableSASLs() {
        SASL names[] = new SASL[recognizedSecurityMethods.size()];
        Enumeration enumeration = recognizedSecurityMethods.elements();
        int count = 0;
        while (enumeration.hasMoreElements()) {
            names[count++] = (SASL)enumeration.nextElement();
        }
        return names;
    }

    public SASL getSASL(String id) {
        return (SASL)recognizedSecurityMethods.get(id);
    }

}

//vi: ai nosi sw=4 ts=4 expandtab