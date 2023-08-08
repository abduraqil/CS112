package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

	/* Your code goes here */

    ArrayList<CharFreq> sortedCharFreqList = new ArrayList<>();

    double[] charCounts = new double[128];
    double totalCount = 0;

    while (StdIn.hasNextChar()) {
        char c = StdIn.readChar();
        charCounts[c]++;
        totalCount++;
    }

    for (int i = 0; i < charCounts.length; i++) {
        if (charCounts[i] > 0) {
            CharFreq charFreq = new CharFreq();
            charFreq.setCharacter((char) i);
            charFreq.setProbOcc(charCounts[i] / totalCount);
            sortedCharFreqList.add(charFreq);
        }
    }

    if (sortedCharFreqList.size() == 1) {
        CharFreq dummyCharFreq = new CharFreq();
        dummyCharFreq.setCharacter((char) ((sortedCharFreqList.get(0).getCharacter() + 1) % 128));
        dummyCharFreq.setProbOcc(0);
        sortedCharFreqList.add(dummyCharFreq);
    }

    Collections.sort(sortedCharFreqList);
    this.sortedCharFreqList = sortedCharFreqList;

    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	/* Your code goes here */

        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        for (int i = 0; i < sortedCharFreqList.size(); i++) {
            TreeNode node = new TreeNode();
            node.setData(sortedCharFreqList.get(i));
            source.enqueue(node); 
        }

        while (!source.isEmpty() || target.size() != 1) {

            TreeNode node = new TreeNode();
            CharFreq newCharFreq = new CharFreq();
            newCharFreq.setCharacter((char) 0);
            TreeNode firstDequeued = null;
            TreeNode secondDequeued = null;;

            if (target.size() == 0) {
                if(source.size() != 1){
                    firstDequeued = source.dequeue();
                    secondDequeued = source.dequeue();
                } else {
                    newCharFreq.setProbOcc(1.0);
                    firstDequeued = source.dequeue();
                }
            } else {
                if (!source.isEmpty()) {
                    double sourceP = source.peek().getData().getProbOcc();
                    double targetP = target.peek().getData().getProbOcc();

                    if (sourceP <= targetP) {
                        firstDequeued = source.dequeue();
                    } else {
                        firstDequeued = target.dequeue();
                    }
                    if (source.isEmpty()) {
                        secondDequeued = target.dequeue();
                    } else {
                        sourceP = source.peek().getData().getProbOcc();
                        if (!target.isEmpty()) {
                            targetP = target.peek().getData().getProbOcc();
                            if (sourceP <= targetP) {
                                secondDequeued = source.dequeue();
                            } else {
                                secondDequeued = target.dequeue();
                            }
                        } else {
                            secondDequeued = source.dequeue();
                        }
                    }
                } else {
                    firstDequeued = target.dequeue();
                    if (!target.isEmpty()) {
                        secondDequeued = target.dequeue();
                    }
                }
            }

            node.setLeft(firstDequeued);
            if (secondDequeued != null) {
                node.setRight(secondDequeued);
                double prob0cc = firstDequeued.getData().getProbOcc() + secondDequeued.getData().getProbOcc();
                newCharFreq.setProbOcc(prob0cc);
            }
            node.setData(newCharFreq);
            target.enqueue(node);
        }
        huffmanRoot = target.peek();
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

	/* Your code goes here */
        encodings = new String [128];
        TreeNode currentNode = huffmanRoot;

        String encoding = "";
        traverseTree(currentNode, encoding, encodings);
	    /* Your code goes here */
    }
    private static void traverseTree(TreeNode currentNode, String encoding, String[] result) { // dunno why i have to do this
        if (currentNode == null) {
            return;
        }
        if (currentNode.getData().getCharacter() != (char) 0) {
            result[currentNode.getData().getCharacter()] = encoding;
        }
        traverseTree(currentNode.getLeft(), encoding + "0", result);
        traverseTree(currentNode.getRight(), encoding + "1", result);
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	/* Your code goes here */
        String encodedString = "";
        while(StdIn.hasNextChar()) {
            char a = StdIn.readChar();
            if (encodings[(int)a] != null) {
                encodedString += encodings[(int)a];
            }
        }    
        writeBitString(encodedFile,encodedString);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */
        String encodeMessage = readBitString(encodedFile);
        char currentBit;
        String resultMessage = "";
        int i = 0;
        TreeNode currentNode = huffmanRoot;
        while(i < encodeMessage.length()) {
            currentBit = encodeMessage.charAt(i);
            if (Character.toString(currentBit).equals("1")) {
                currentNode = currentNode.getRight();
                if (currentNode.getData().getCharacter() != (char) 0) {
                    resultMessage += currentNode.getData().getCharacter();
                    currentNode = huffmanRoot;
                }
            } else {
                currentNode = currentNode.getLeft();
                if (currentNode.getData().getCharacter() != (char) 0) {
                    resultMessage += currentNode.getData().getCharacter();
                    currentNode = huffmanRoot;
                }
            }
            i++;
        }
        StdOut.print(resultMessage);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
