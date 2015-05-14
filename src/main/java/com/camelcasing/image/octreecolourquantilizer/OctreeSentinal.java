package com.camelcasing.image.octreecolourquantilizer;

public class OctreeSentinal {
	
		private OctreeNode root;
		private int size;
		private int maxColours;
	
	protected OctreeSentinal(int maxColours){
		this.maxColours = maxColours;
		root = new OctreeNode(null, 0);
		root.setNext(root);
		root.setPrevious(root);
	}
	
	protected void addToNode(int[] colours, int r, int g, int b){
		OctreeNode node = root.getChild(colours[0])
				.getChild(colours[1])
				.getChild(colours[2])
				.getChild(colours[3])
				.getChild(colours[4])
				.getChild(colours[5])
				.getChild(colours[6])
				.getChild(colours[7])
				.addColour(r, g, b);
		
		if(!node.isOnPointerList()){
			node.setOnPointerList(true);
			addToEnd(node);
		}
	}
	
	protected OctreeNode getNext(){
		return root.getNext();
	}
	
	protected OctreeNode getRoot(){
		return root;
	}
	
	private OctreeNode getLast(){
		return root.getPrevious();
	}
	
	protected OctreeNode getStartNode(int index){
		OctreeNode otn = root.getChild(index);
		if(otn != null) return otn;
		int left = index;
			if(index > 0) left = index - 1;
		int right = index;
			if(index < 7) right = index + 1; 
		while(true){
			otn = root.getChild(right);
			if(otn != null) return otn;
			otn = root.getChild(left);
			if(otn != null) return otn;
			
			if(left > 0) left = index - 1;
			if(right < 7) right = index + 1;
		}
	}
	
	private void addToEnd(OctreeNode otn){
		getLast().setNext(otn);
		otn.setNext(root.getNext());
		otn.setPrevious(getLast());
		root.setPrevious(otn);
		size++;
	}
	
	private void remove(){
		OctreeNode otn = getNext();
		otn.getNext().setPrevious(root);
		root.setNext(otn.getNext());
		otn.mergeUp();
		if(!otn.getParent().isOnPointerList()){
			addToEnd(otn.getParent());
			otn.getParent().setOnPointerList(true);
		}
		otn = null;
		--size;
	}
	
	protected void prune(){
		while(size > maxColours){
			remove();
		}
		assignPointerListIndexes();
	}
	
	private void assignPointerListIndexes(){
		OctreeNode n = getNext();
		for(int i = 0; i < size; i++){
			n.setPointerListIndex(i);
			n = n.getNext();
		}
	}
	
	protected int getSize(){
		return size;
	}
}
