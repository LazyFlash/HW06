import java.io.*;
import java.util.LinkedList;

class Stack{
	LinkedList<Node> stk;
	int top;
	
	public Stack(){
		this.stk = new LinkedList<Node>();
		this.top = 0;
	}
	
	public void push(Node n){
		this.stk.add(n);
		top++;
	}
	
	public Node pop(){
		Node rt = this.stk.get(--top);
		this.stk.remove(top);
		return rt;
	}
	
	public boolean isEmpty(){
		return top == 0;
	}
}

class Node {
	public int val;
	public Node left;
	public Node right;
	public Node parent;
	public boolean isBlack;
	
	public Node(){
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isBlack = true;
	}
	
	public Node(int val){
		this();
		this.val = val;
		this.isBlack = false;
	}
}

public class RbTree {
	public Node nil = new Node();
	public Node root = nil;
	public int inserted = 0;
	public int deleted = 0;
	public int missed = 0;
	
	public RbTree(){
		root.left = nil;
		root.right = nil;
		root.parent = nil;
	}
	
	public void insert(Node tree, Node n){
		Node x = root;
		Node y = nil;
		
		while(x != nil){
			y = x;
			if(n.val < x.val){
				x = x.left;
			}else{
				x = x.right;
			}
		}
		n.parent = y;
		
		if(y == nil){
			root = n;
		}else if(n.val < y.val){
			y.left = n;
		}else{
			y.right = n;
		}
		n.left = nil;
		n.right = nil;
		rbInsertFixUp(tree, n);
		this.inserted++;
	}
	
	public void rbInsertFixUp(Node tree, Node n){
		while(!n.parent.isBlack){
			if(n.parent == n.parent.parent.left){
				Node uncle = n.parent.parent.right;
				
				if(!uncle.isBlack){
					n.parent.isBlack = true;
					uncle.isBlack = true;
					n.parent.parent.isBlack = false;
					n = n.parent.parent;
				}else if(n == n.parent.right){
						n = n.parent;
						leftRotate(tree, n);
				}else{
					n.parent.isBlack = true;
					n.parent.parent.isBlack = false;
					rightRotate(tree, n.parent.parent);
				}
			}else{
				Node uncle = n.parent.parent.left;
				
				if(!uncle.isBlack){
					n.parent.isBlack = true;
					uncle.isBlack = true;
					n.parent.parent.isBlack = false;
					n = n.parent.parent;
				}else if(n == n.parent.left){
						n = n.parent;
						rightRotate(tree, n);
				}else{
					n.parent.isBlack = true;
					n.parent.parent.isBlack = false;
					leftRotate(tree, n.parent.parent);
				}
			}
		}
		root.isBlack = true;
	}
	
	public void leftRotate(Node tree, Node n){
		Node y = n.right;
		n.right = y.left;
		
		if(y.left != nil){
			y.left.parent = n;
		}
		y.parent = n.parent;

		if(n.parent == nil){
			this.root = y;
		}else if(n == n.parent.left){
			n.parent.left = y;
		}else{
			n.parent.right = y;
		}
		y.left = n;
		n.parent = y;
	}
	
	public void rightRotate(Node tree, Node n){
		Node y = n.left;
		n.left = y.right;
		
		if(y.right != nil){
			y.right.parent = n;
		}
		y.parent = n.parent;

		if(n.parent == nil){
			this.root = y;
		}else if(n == n.parent.left){
			n.parent.left = y;
		}else{
			n.parent.right = y;
		}
		y.right = n;
		n.parent = y;
	}
	
	public void transplant(Node tree, Node obj, Node trns){
		if(obj.parent == nil){
			root = trns;
		}else if(obj == obj.parent.left){
			obj.parent.left = trns;
		}else if(obj == obj.parent.right){
			obj.parent.right = trns;
		}
		trns.parent = obj.parent;
		
	}
	
	public Node min(Node tree){
		while(tree.left != nil){
			tree = tree.left;
		}
		return tree;
	}
	
	public Node max(Node tree){
		while(tree.right != nil){
			tree = tree.right;
		}
		return tree;
	}
	
	public void delete(Node tree, int key){
		Node n = search(tree, key);
		if(n == nil){
			this.missed++;
			return;
		}
		Node x = nil;
		Node y = n;
		boolean yOriginallyBlack = y.isBlack;
		
		if(n.left == nil){
			x = n.right;
			transplant(tree, n, n.right);
		}else if(n.right == nil){
			x = n.left;
			transplant(tree, n, n.left);
		}else{
			y = min(n.right);
			yOriginallyBlack = y.isBlack;
			x = y.right;
			if(y.parent == n){
				x.parent = y;
			}else{
				transplant(tree, y, y.right);
				y.right = n.right;
				y.right.parent = y;
			}
			transplant(tree, n, y);
			y.left = n.left;
			y.left.parent = y;
			y.isBlack = n.isBlack;
		}
		
		if(yOriginallyBlack){
			rbDeleteFixUp(tree, x);
		}
		this.deleted++;
	}
	
	public void rbDeleteFixUp(Node tree, Node n){
		Node w;
		
		while(n != root && n.isBlack){
			
			if(n == n.parent.left){
				w = n.parent.right;
				if(!w.isBlack){
					w.isBlack = true;
					n.parent.isBlack = false;
					leftRotate(tree, n.parent);
					w = n.parent.right;
				}
				
				if(w.left.isBlack && w.right.isBlack){
					w.isBlack = false;
					n = n.parent;
				}else{
					if(w.right.isBlack){
						w.left.isBlack = true;
						w.isBlack = false;
						rightRotate(tree, w);
						w = n.parent.right;
					}
					w.isBlack = n.parent.isBlack;
					n.parent.isBlack = true;
					w.right.isBlack = true;
					leftRotate(tree, n.parent);
					n = root;
				}
			}else{
				w = n.parent.left;
				if(!w.isBlack){
					w.isBlack = true;
					n.parent.isBlack = false;
					rightRotate(tree, n.parent);
					w = n.parent.left;
				}
				
				if(w.right.isBlack && w.left.isBlack){
					w.isBlack = false;
					n = n.parent;
				}else{
					if(w.left.isBlack){
						w.right.isBlack = true;
						w.isBlack = false;
						leftRotate(tree, w);
						w = n.parent.left;
					}
					w.isBlack = n.parent.isBlack;
					n.parent.isBlack = true;
					w.left.isBlack = true;
					rightRotate(tree, n.parent);
					n = root;
				}
			}
		}
		n.isBlack = true;
	}
	
	public Node search(Node tree, int key){
		while(tree != nil){
			if(tree.val == key){
				return tree;
			}else if(tree.val > key){
				tree = tree.left;
			}else{
				tree = tree.right;
			}
		}
		return nil;
	}
	
	public Node whereNilFrom(Node tree, int key){
		Node from = new Node();
		while(tree != nil){
			if(tree.val > key){
				from = tree;
				tree = tree.left;
			}else{
				from = tree;
				tree = tree.right;
			}
		}
		return from;
	}

	public void inorder(Node tree){
		Stack stk = new Stack();
		while(!stk.isEmpty() || tree != nil){
			if(tree != nil){
				stk.push(tree);
				tree = tree.left;
			}else{
				tree = stk.pop();
				if(tree.isBlack){
					System.out.println(tree.val + " B");
				}else{
					System.out.println(tree.val + " R");
				}
				tree = tree.right;
			}
		}
	}
	
	public int total(Node tree){
		Stack stk = new Stack();
		int total = 0;
		while(!stk.isEmpty() || tree != nil){
			if(tree != nil){
				stk.push(tree);
				tree = tree.left;
			}else{
				tree = stk.pop();
				total = total + 1;
				tree = tree.right;
			}
		}
		return total;
	}
	
	public int nb(Node tree){
		Stack stk = new Stack();
		int nb = 0;
		while(!stk.isEmpty() || tree != nil){
			if(tree != nil){
				stk.push(tree);
				tree = tree.left;
			}else{
				tree = stk.pop();
				if(tree.isBlack){
					nb++;
				}
				tree = tree.right;
			}
		}
		return nb;
	}
	
	public int bh(Node tree){
		if(tree == nil){
			return 0;
		}
		int bh = 1;
		while(tree.left != nil && tree.right != nil){
			if(tree.left == nil){
				tree = tree.right;
			}else{
				tree = tree.left;
			}
			if(tree.isBlack){
				bh++;
			}
		}
		
		return bh;
	}
	
	public void print(Node tree, int level){
		if(tree.right != nil){
			print(tree.right, level + 1);
		}
		for(int i = 0; i < level; i++){
			System.out.print("        ");			
		}
		System.out.println(tree.val + "" + tree.isBlack);
		if(tree.left != nil){
			print(tree.left, level + 1);
		}
	}
	
	public Node findSuccessor(Node tree){
		if(tree == nil){
			return nil;
		}
		
		if(tree.right != nil){
			return min(tree.right);
		}
		
		Node y = tree.parent;
		Node x = tree;
		while (y != nil && x == y.right)
		{
			x = y;
			y = y.parent;
		}
		
		return y;
	}
	
	public Node findPredecessor(Node tree){
		if(tree == nil){
			return nil;
		}
		
		if(tree.left != nil){
			return max(tree.left);
		}
		
		Node y = tree.parent;
		Node x = tree;
		while (y != nil && x == y.left)
		{
			x = y;
			y = y.parent;
		}
		
		return y;
	}
	
	public String printPAndS(int key){
		Node tree = search(root, key);
		String result;
		if(tree == nil){
			Node from = whereNilFrom(root, key);
			if(from.val > key){
				Node pred = findPredecessor(from);
				if(pred == nil){
					System.out.println("NIL NIL " + from.val);
					result = "NIL NIL " + from.val;
				}else{
					System.out.println(pred.val + " NIL " + from.val);
					result = pred.val + " NIL " + from.val;
				}
			}else{
				Node succ = findSuccessor(from);
				if(succ == nil){
					System.out.println(from.val + " NIL NIL");
					result = from.val + " NIL NIL";
				}else{
					System.out.println(from.val + " NIL " + succ.val);
					result = from.val + " NIL " + succ.val;
				}
			}
		}else{
			Node pred = findPredecessor(tree);
			Node succ = findSuccessor(tree);
			if(succ == nil){
				System.out.println(pred == nil ? "NIL " + tree.val + " NIL" : "" + pred.val +  " "+ tree.val + " NIL");
				result = pred == nil ? "NIL " + tree.val + " NIL" : "" + pred.val +  " "+ tree.val + " NIL";
			}else{
				System.out.println(pred == nil ? "NIL " + tree.val + " " + succ.val : "" + pred.val +  " "+ tree.val + " " + succ.val);
				result = pred == nil ? "NIL " + tree.val + " " + succ.val : "" + pred.val +  " "+ tree.val + " " + succ.val;
			}
		}
		
		return result;
	}
	
	public static void main(String [] args) throws IOException{
		File in = new File("test01.txt"); 
		File search = new File("search01.txt");
		File out = new File("output.txt");

		try{
			BufferedReader br = new BufferedReader(new FileReader(in));
			BufferedWriter wr = new BufferedWriter(new FileWriter(out));
			BufferedReader s = new BufferedReader(new FileReader(search));
			
			RbTree r = new RbTree();
			while(true){
				String inline = br.readLine();
				String line = inline.trim();
				int num = Integer.parseInt(line);
				if(num > 0){
					r.insert(r.root, new Node(num));
				}else if(num < 0){
					r.delete(r.root, num * -1);
				}else if(num == 0){
					break;
				}
			}
			while(true){
				String val = s.readLine();
				val = val.trim();
				int search_val = Integer.parseInt(val);
				if(search_val == 0){
					break;
				}
				wr.write(r.printPAndS(search_val));
				wr.newLine();
			}
			
			br.close();
			wr.close();
			s.close();
		}catch(IOException e){

		}
	}
}
