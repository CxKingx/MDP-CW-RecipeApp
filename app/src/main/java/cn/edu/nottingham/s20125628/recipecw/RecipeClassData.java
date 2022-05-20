package cn.edu.nottingham.s20125628.recipecw;

public class RecipeClassData implements Comparable<RecipeClassData>{
    String ID,Title,Description,Rating,Ingredients,VideoTitle,VideoPath;

    public RecipeClassData(String Title,String Description,String Rating,String Ingredients,String VideoTitle,String VideoPath){
        this.ID="1";
        this.Title=Title;
        this.Description=Description;
        this.Rating=Rating;
        this.Ingredients=Ingredients;
        this.VideoTitle=VideoTitle;
        this.VideoPath=VideoPath;
    }
    public String getID(){
        return ID;
    }
    public void setID(String ID){
        this.ID=ID;
    }
    public String getTitle(){
        return Title;
    }
    public void setTitle(String Title){
        this.ID=Title;
    }
    public String getDescription(){
        return Description;
    }
    public void setDescription(String Description){
        this.Description=Description;
    }
    public String getRating(){
        return Rating;
    }
    public void setRating(String Rating){
        this.Rating=Rating;
    }
    public String getIngredients(){
        return Ingredients;
    }
    public void setIngredients(String Ingredients){
        this.Ingredients=Ingredients;
    }
    public String getVideoTitle(){
        return VideoTitle;
    }
    public void setVideoTitle(String VideoTitle){
        this.VideoTitle=VideoTitle;
    }
    public String getVideoPath(){
        return VideoPath;
    }
    public void setVideoPath(String VideoPath){
        this.VideoPath=VideoPath;
    }

    @Override
    public String toString() {
        return "RecipeClassData{" +
                "ID='" + ID + '\'' +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Rating='" + Rating + '\'' +
                ", Ingredients='" + Ingredients + '\'' +
                ", VideoTitle='" + VideoTitle + '\'' +
                ", VideoPath='" + VideoPath + '\'' +
                '}';
    }

    @Override
    public int compareTo(RecipeClassData recipeClassData) {
        float compareRating = Float.parseFloat(recipeClassData.getRating());
        int IntegerCompareRating = (int)(compareRating*100);
        //return ((int)(Float.parseFloat(this.getRating())*100) - IntegerCompareRating); //Ascend
        return (IntegerCompareRating- (int)(Float.parseFloat(this.getRating())*100) );     //Descend

    }
}
