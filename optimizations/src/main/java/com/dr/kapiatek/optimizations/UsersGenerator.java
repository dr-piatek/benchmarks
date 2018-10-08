/**
 *
 */
package com.dr.kapiatek.optimizations;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import javolution.osgi.internal.OSGiServices;
import javolution.text.TextBuilder;

/**
 * @author ekarpia
 *
 */
public class UsersGenerator {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        if (args.length != 1) {
            System.out.println("Please provide which XML provider to use JRE or ALT");
            System.exit(1);
        }

        final File outputFolder = new File(System.getenv("HOME") + "/Users");
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        final boolean isJRE = "JRE".equals(args[0]) ? true : false;

        final int startUser = 1;
        final int endUser = startUser + 1000;

        if (isJRE) {
            generateUsersImportFileJavaApi(outputFolder, startUser, endUser);
        } else {
            generateUsersImportFileJavolution(outputFolder, startUser, endUser);
        }

    }

    private static final String OUTPUT_FILE_FORMAT = "%s/users_list_%d_%d_%s.xml";

    /**
     *
     * @param outputFolder
     * @param startUser
     * @param endUser
     */
    private static void generateUsersImportFileJavolution(final File outputFolder, final int startUser, final int endUser) {
        OutputStream out;
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(String.format(OUTPUT_FILE_FORMAT, outputFolder.getAbsolutePath(), startUser, endUser - 1, "Javolution")));

            final javolution.xml.stream.XMLOutputFactory factory = OSGiServices.getXMLOutputFactory();
            factory.setProperty(javolution.xml.stream.XMLOutputFactory.INDENTATION, "\t");
            final javolution.xml.stream.XMLStreamWriter writer = factory.createXMLStreamWriter(out);

            final TextBuilder tmp = new TextBuilder(); // To avoid creating new String instances.
            writer.writeStartDocument();

            writer.writeStartElement("users"); //open Users

            for (int i = startUser; i < endUser; i++) {

                final String userName = "TestUser" + i;

                writer.writeStartElement("user"); //open User

                writer.writeStartElement("name");
                writer.writeCharacters(tmp.clear().append(userName));
                writer.writeEndElement();

                writer.writeStartElement("password");
                writer.writeCharacters(tmp.clear().append("password1234"));
                writer.writeEndElement();

                writer.writeStartElement("status");
                writer.writeCharacters(tmp.clear().append("enabled"));
                writer.writeEndElement();

                writer.writeStartElement("firstname");
                writer.writeCharacters(tmp.clear().append(userName));
                writer.writeEndElement();

                writer.writeStartElement("surname");
                writer.writeCharacters(tmp.clear().append(userName));
                writer.writeEndElement();

                writer.writeStartElement("email");
                writer.writeCharacters(tmp.clear().append(userName).append("@epsilon.gc"));
                writer.writeEndElement();

                writer.writeEmptyElement("description");

                writer.writeEndElement(); //close User
            }

            writer.writeEndElement(); //close Users
            writer.writeEndDocument();

            writer.close(); // Closes the writer (does not close underlying output stream).

            out.close();

        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final javolution.xml.stream.XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param outputFolder
     * @param startUser
     * @param endUser
     */
    private static void generateUsersImportFileJavaApi(final File outputFolder, final int startUser, final int endUser) {
        OutputStream out;
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(String.format(OUTPUT_FILE_FORMAT, outputFolder.getAbsolutePath(), startUser, endUser - 1, "JRE")));

            final XMLOutputFactory factory = XMLOutputFactory.newInstance();

            final XMLStreamWriter writer = factory.createXMLStreamWriter(out);

            writer.writeStartDocument();

            writer.writeStartElement("users"); //open Users

            for (int i = startUser; i < endUser; i++) {

                final String userName = "TestUser" + i;

                writer.writeStartElement("user"); //open User

                writer.writeStartElement("name");
                writer.writeCharacters(userName);
                writer.writeEndElement();

                writer.writeStartElement("password");
                writer.writeCharacters("password1234");
                writer.writeEndElement();

                writer.writeStartElement("status");
                writer.writeCharacters("enabled");
                writer.writeEndElement();

                writer.writeStartElement("firstname");
                writer.writeCharacters(userName);
                writer.writeEndElement();

                writer.writeStartElement("surname");
                writer.writeCharacters(userName);
                writer.writeEndElement();

                writer.writeStartElement("email");
                writer.writeCharacters(userName + "@epsilon.gc");
                writer.writeEndElement();

                writer.writeEmptyElement("description");

                writer.writeEndElement(); //close User
            }

            writer.writeEndElement(); //close Users
            writer.writeEndDocument();

            writer.close(); // Closes the writer (does not close underlying output stream).

            out.close();

        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final javax.xml.stream.XMLStreamException e) {
            e.printStackTrace();
        }
    }

}
