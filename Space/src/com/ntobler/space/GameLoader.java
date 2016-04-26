package com.ntobler.space;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GameLoader {

	private static final String FILLE_NAME = "space.xml";
	
		
	public static void saveWorkspace(PhysicalWorkspace w) throws Exception{
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(FILLE_NAME)));
        encoder.writeObject(w);
        encoder.close();
    }

    public static PhysicalWorkspace loadWorkspace() throws Exception {
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(FILLE_NAME)));
        PhysicalWorkspace w = (PhysicalWorkspace)decoder.readObject();
        decoder.close();
        return w;
    }
	
}
