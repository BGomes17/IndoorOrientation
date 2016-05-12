package com.northteam.indoororientation.model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author beatrizgomes
 *         Date 09/05/2016
 */
public class XmlParser {
    String name;
    String id;

    private static final String ns = null;

    // We don't use namespaces

    /* PLACES */

    /**
     * @param in the stream to parse
     * @return the list of places in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Place> parsePlaces(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPlaces(parser);
        } finally {
            in.close();
        }
    }

    /**
     * @param parser
     * @return the list of places in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Place> readPlaces(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Place> entries = new ArrayList<Place>();

        parser.require(XmlPullParser.START_TAG, ns, "places");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the place tag
            if (name.equals("place")) {
                entries.add(readPlace(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     * Parses the contents of an entry. If it encounters a id, or name tag, hands them
     * off to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
     *
     * @param parser
     * @return the place in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Place readPlace(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "place");
        String id = null;
        String name = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String text = parser.getName();
            if (text.equals("id")) {
                id = readIdPlace(parser);
            } else if (text.equals("name")) {
                name = readNamePlace(parser);
            } else {
                skip(parser);
            }
        }
        return new Place(id, name);
    }

    /**
     * Processes id tags in the xml file
     *
     * @param parser
     * @return id of the place
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readIdPlace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    /**
     * Processes name tags in the feed.
     *
     * @param parser
     * @return name of the place
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readNamePlace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    // For the tags id and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /* BEACONS */

    /**
     * @param parser
     * @return the list of beacons in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Beacon> readBeacons(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Beacon> entries = new ArrayList<Beacon>();

        parser.require(XmlPullParser.START_TAG, ns, "beacons");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the place tag
            if (name.equals("beacon")) {
                entries.add(readBeacon(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     * Parses the contents of an entry. If it encounters a id, name, uniqueId or namePlace tag, hands them
     * off to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
     *
     * @param parser
     * @return the beacon in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Beacon readBeacon(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "beacon");
        int id = -1;
        String name = null;
        String uniqueId = null;
        String namePlace = null;
        List<Edge> edges = null;
        List<NearPlace> nearPlaces = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String text = parser.getName();
            if (text.equals("id")) {
                id = readIdBeacon(parser);
            } else if (text.equals("uniqueId")) {
                uniqueId = readUniqueIdBeacon(parser);
            } else if (text.equals("name")) {
                name = readNameBeacon(parser);
            } else if (text.equals("namePlace")) {
                namePlace = readNameLocalBeacon(parser);
            } else if (text.equals("edges")) {
                edges = readEdges(parser);
            } else if (text.equals("namePlace")) {
                nearPlaces = readNearPlaces(parser);
            } else {
                skip(parser);
            }
        }
        Beacon b = new Beacon(id, uniqueId, name, namePlace);
        for (Edge e : edges) {
            b.addAdj(e);
        }
        for (NearPlace np : nearPlaces) {
            b.addAdjNear(np);
        }

        return b;
    }

    /**
     * Processes id tags in the xml file
     *
     * @param parser
     * @return id of the beacon
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readIdBeacon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return Integer.parseInt(id);
    }

    /**
     * Processes uniqueId tags in the feed.
     *
     * @param parser
     * @return uniqueId of the beacon
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readUniqueIdBeacon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "uniqueId");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "uniqueId");
        return name;
    }

    /**
     * Processes name tags in the feed.
     *
     * @param parser
     * @return name of the beacon
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readNameBeacon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    /**
     * Processes nameLocal tags in the feed.
     *
     * @param parser
     * @return nameLocal of the beacon
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readNameLocalBeacon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nameLocal");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nameLocal");
        return name;
    }

    /* EDGES */

    /**
     * @param parser
     * @return the list of edges in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Edge> readEdges(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Edge> edges = new ArrayList<Edge>();

        parser.require(XmlPullParser.START_TAG, ns, "edges");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the place tag
            if (name.equals("edge")) {
                edges.add(readEdge(parser));
            } else {
                skip(parser);
            }
        }
        return edges;
    }

    /**
     * Parses the contents of an entry. If it encounters a id, name, uniqueId or namePlace tag, hands them
     * off to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
     *
     * @param parser
     * @return the edge in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Edge readEdge(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "edge");
        int v = -1;
        int w = -1;
        float weight = 0.0f;
        char compass = '\u0000';
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String text = parser.getName();
            if (text.equals("v")) {
                v = readVEdge(parser);
            } else if (text.equals("w")) {
                w = readWEdge(parser);
            } else if (text.equals("weight")) {
                weight = readWeightEdge(parser);
            } else if (text.equals("compass")) {
                compass = readCompassEdge(parser);
            } else {
                skip(parser);
            }
        }

        return new Edge(v, w, weight, compass);
    }

    /**
     * Processes v tags in the xml file
     *
     * @param parser
     * @return v of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readVEdge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "v");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "v");
        return Integer.parseInt(id);
    }

    /**
     * Processes id tags in the xml file
     *
     * @param parser
     * @return w of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readWEdge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "w");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "w");
        return Integer.parseInt(id);
    }

    /**
     * Processes weight tags in the xml file
     *
     * @param parser
     * @return weight of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private float readWeightEdge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "weight");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "weight");
        return Float.parseFloat(id);
    }

    /**
     * Processes compass tags in the feed.
     *
     * @param parser
     * @return compass of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private char readCompassEdge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "compass");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "compass");
        return name.charAt(0);
    }

    /* NEARPLACES */


    /**
     * @param parser
     * @return the list of edges in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<NearPlace> readNearPlaces(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<NearPlace> nPlaces = new ArrayList<NearPlace>();

        parser.require(XmlPullParser.START_TAG, ns, "nearPlaces");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the place tag
            if (name.equals("nearPlace")) {
                nPlaces.add(readNearPlace(parser));
            } else {
                skip(parser);
            }
        }
        return nPlaces;
    }

    /**
     * Parses the contents of an entry. If it encounters a id, name, uniqueId or namePlace tag, hands them
     * off to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
     *
     * @param parser
     * @return the edge in the xml file
     * @throws XmlPullParserException
     * @throws IOException
     */
    private NearPlace readNearPlace(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "nearPlace");
        int id = -1;
        float distance = 0.0f;
        char compass = '\u0000';
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String text = parser.getName();
            if (text.equals("idPlace")) {
                id = readIdNearPlace(parser);
            } else if (text.equals("proximityDistance")) {
                distance = readDistanceNearPlace(parser);
            } else if (text.equals("compass")) {
                compass = readCompassNearPLace(parser);
            } else {
                skip(parser);
            }
        }

        return new NearPlace(id, distance, compass);
    }

    /**
     * Processes idPlace tags in the xml file
     *
     * @param parser
     * @return idPlace of the near place
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readIdNearPlace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "idPlace");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "idPlace");
        return Integer.parseInt(id);
    }

    /**
     * Processes weight tags in the xml file
     *
     * @param parser
     * @return weight of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private float readDistanceNearPlace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "proximityDistance");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "proximityDistance");
        return Float.parseFloat(id);
    }

    /**
     * Processes compass tags in the feed.
     *
     * @param parser
     * @return compass of the edge
     * @throws IOException
     * @throws XmlPullParserException
     */
    private char readCompassNearPLace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "compass");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "compass");
        return name.charAt(0);
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}


