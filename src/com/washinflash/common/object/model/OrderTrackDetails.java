package com.washinflash.common.object.model;

public class OrderTrackDetails {

		private boolean isPickedUp;
		private boolean isWashed;
		private boolean isPackaged;
		private boolean isDelivered;
		
		private String overallStatus;		
		
		private String pickUpMessage;
		private String washMessage;
		private String packagingMessage;
		private String deliveryMessage;
		
		private boolean isCancellable;
		private boolean isReviewable;
		
		public boolean isPickedUp() {
			return isPickedUp;
		}
		public void setPickedUp(boolean isPickedUp) {
			this.isPickedUp = isPickedUp;
		}
		public boolean isWashed() {
			return isWashed;
		}
		public void setWashed(boolean isWashed) {
			this.isWashed = isWashed;
		}
		public boolean isPackaged() {
			return isPackaged;
		}
		public void setPackaged(boolean isPackaged) {
			this.isPackaged = isPackaged;
		}
		public boolean isDelivered() {
			return isDelivered;
		}
		public void setDelivered(boolean isDelivered) {
			this.isDelivered = isDelivered;
		}
		public String getOverallStatus() {
			return overallStatus;
		}
		public void setOverallStatus(String overallStatus) {
			this.overallStatus = overallStatus;
		}
		public String getPickUpMessage() {
			return pickUpMessage;
		}
		public void setPickUpMessage(String pickUpMessage) {
			this.pickUpMessage = pickUpMessage;
		}
		public String getWashMessage() {
			return washMessage;
		}
		public void setWashMessage(String washMessage) {
			this.washMessage = washMessage;
		}
		public String getPackagingMessage() {
			return packagingMessage;
		}
		public void setPackagingMessage(String packagingMessage) {
			this.packagingMessage = packagingMessage;
		}
		public String getDeliveryMessage() {
			return deliveryMessage;
		}
		public void setDeliveryMessage(String deliveryMessage) {
			this.deliveryMessage = deliveryMessage;
		}
		public boolean isCancellable() {
			return isCancellable;
		}
		public void setCancellable(boolean isCancellable) {
			this.isCancellable = isCancellable;
		}
		public boolean isReviewable() {
			return isReviewable;
		}
		public void setReviewable(boolean isReviewable) {
			this.isReviewable = isReviewable;
		}
		
}
