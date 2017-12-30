import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Vertex {
    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }

    public int getBranchWeight(){
        return branchWeight;
    }
    int weight;
    int branchWeight=0;
    ArrayList<Integer> children;
}

class PlanParty {
    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }

    static void dfs(Vertex[] tree, int vertex, int parent) {
//        System.out.printf("vertex = %d, parent = %d%n", vertex, parent); //debug
        int grandChildSum = tree[vertex].weight;
        int childSum = 0;
        for (int child : tree[vertex].children)
            if (child != parent){
                dfs(tree, child, vertex);
                for(int gChild:tree[child].children){
                    if(gChild!=parent){
                        grandChildSum += tree[gChild].branchWeight;
                    }
                }
//                grandChildSum += tree[child].children.stream()
//                    .filter(gchild->parent!=gchild)
//                    .mapToInt(gchild->tree[gchild].getBranchWeight())
//                    .sum();
            }
        // This is a template function for processing a tree using depth-first search.
        // Write your code here.
        // You may need to add more parameters to this function for child processing.
        
        if(tree[vertex].children.size()==1 && tree[vertex].children.get(0)==parent){
            tree[vertex].branchWeight = tree[vertex].weight;
        }
        else
        {
            for(int child:tree[vertex].children){
                if(child!=parent){
                    childSum+=tree[child].branchWeight;
                }
            }
//            int childSum = tree[vertex].children.stream()
//                    .filter(child->parent!=child)
//                    .mapToInt(child->tree[child].getBranchWeight())
//                    .sum();
            tree[vertex].branchWeight = Math.max(childSum, grandChildSum);
        }
            
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        int size = tree.length;
        if (size == 0)
            return 0;
        dfs(tree, 0, -1);
        
        return tree[0].branchWeight;
    }

    public static void main(String[] args) throws IOException {
      // This is to avoid stack overflow issues
      new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new PlanParty().run();
                        } catch (IOException ex) {
                            Logger.getLogger(PlanParty.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}
