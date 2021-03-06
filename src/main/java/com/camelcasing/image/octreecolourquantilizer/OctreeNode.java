package com.camelcasing.image.octreecolourquantilizer;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class OctreeNode{
	
		private int count = 0;
		private OctreeNode[] children = new OctreeNode[8];
		private OctreeNode parent;
		private int red, green, blue;
		private int index;
		private boolean isOnPointerList;
		private OctreeNode next;
		private OctreeNode previous;
		private int pointerListIndex = -1;
	
	public OctreeNode(OctreeNode parent, int index){
		this.parent = parent;
		this.index = index;
	}

	public OctreeNode addColour(int r, int g, int b, int count){
		this.count += count;
		red += r;
		green += g;
		blue += b;
		return this;
	}
	
	/**
	 * Adds RGB values to the node and increments the count by one
	 * @param r red value
	 * @param g green value
	 * @param b blue value
	 * @return this
	 */
	public OctreeNode addColour(int r, int g, int b){
		addColour(r, g, b, 1);
		return this;
	}
	/**
	 * 
	 * @param index Index of this node in Parents Children
	 * @return this
	 */
	public OctreeNode getChild(int index){
			if(children[index] == null){
				children[index] = new OctreeNode(this, index);
			}
		return children[index];
	}

	/**
	 * 	does return null often, but that is the point.
	 * 
	 * @param index of the child (0-7)
	 * @return Then Octree node for the given index or null if child does not exist
	 */
	public OctreeNode getChildIfExists(int index) throws IllegalArgumentException{
			if(index < 0 || index > 7){
				throw new IllegalArgumentException("Index must be between 0 and 7");
			}
		return children[index];
	}
	
	public void mergeUp(){
		parent.addColour(red, green, blue, count);
		parent.kill(index);
	}
	
	/**
	 * @param b declares this node as the bottom of the tree and holder of a colour values 
	 */
	public void setOnPointerList(boolean b){
		isOnPointerList = b;
	}
	
	/**
	 * 
	 * @return if this node is at the bottom of the tree and holds actual colour values
	 */
	public boolean isOnPointerList(){
		return isOnPointerList;
	}
	
	public OctreeNode getNext(){
		return next;
	}
	
	public OctreeNode getPrevious(){
		return previous;
	}
	
	public void setNext(OctreeNode otn){
		next = otn;
	}
	
	public void setPrevious(OctreeNode otn){
		previous = otn;
	}
	
	public void kill(int index){
		children[index] = null;
	}
	
	public OctreeNode getParent(){
		return parent;
	}
	
	public int getColourCount(){
		return count;
	}

	public int getRed() {
		if(count == 0) count = 1;
		return red / count;
	}

	public int getGreen() {
		if(count == 0) count = 1;
		return green / count;
	}

	public int getBlue() {
		if(count == 0) count = 1;
		return blue / count;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setPointerListIndex(int i){
		pointerListIndex = i;
	}
	
	public int getPointerListIndex(){
		return pointerListIndex;
	}
	
	@Override
	public String toString(){
		return("OctreeNode, index = " + index + ", count = " + count +
				", red = " + getRed() + ", green = " + getGreen() + ", blue = " + getBlue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + count;
		result = prime * result + green;
		result = prime * result + index;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + red;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OctreeNode other = (OctreeNode) obj;
		if (blue != other.blue)
			return false;
		if (count != other.count)
			return false;
		if (green != other.green)
			return false;
		if (index != other.index)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (red != other.red)
			return false;
		return true;
	}

}
