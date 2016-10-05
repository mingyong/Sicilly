package xyz.shaohui.sicilly.data.models;

/**
 * Created by shaohui on 16/10/5.
 */

public class FriendshipDetail {

    private RelationShip relationship;

    public boolean isFollowedEach() {
        return relationship.getSource().isFollowing() && relationship.getSource().isFollowed_by();
    }

    public RelationShip getRelationship() {
        return relationship;
    }

    public void setRelationship(RelationShip relationship) {
        this.relationship = relationship;
    }

    private class RelationShip {
        Detail source;
        Detail target;

        public Detail getSource() {
            return source;
        }

        public void setSource(Detail source) {
            this.source = source;
        }

        public Detail getTarget() {
            return target;
        }

        public void setTarget(Detail target) {
            this.target = target;
        }
    }

    private class Detail {

        boolean following ;

        boolean followed_by;

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        public boolean isFollowed_by() {
            return followed_by;
        }

        public void setFollowed_by(boolean followed_by) {
            this.followed_by = followed_by;
        }
    }
}
