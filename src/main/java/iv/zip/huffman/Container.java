package iv.zip.huffman;

import iv.zip.huffman.node.Node;

import java.util.List;

public class Container {
    Node root;
    int[] zip;

    public Container(List<Integer> list, Node root) {

        this.root = root;
        this.zip = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            zip[i] = list.get(i);
    }

    public Node getRoot() {
        return root;
    }

    public int[] getZip() {
        return zip;
    }
}
