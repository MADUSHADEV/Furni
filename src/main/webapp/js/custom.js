(function () {
  "use strict";

  var tinyslider = function () {
    var el = document.querySelectorAll(".testimonial-slider");

    if (el.length > 0) {
      var slider = tns({
        container: ".testimonial-slider",
        items: 1,
        axis: "horizontal",
        controlsContainer: "#testimonial-nav",
        swipeAngle: false,
        speed: 700,
        nav: true,
        controls: true,
        autoplay: true,
        autoplayHoverPause: true,
        autoplayTimeout: 3500,
        autoplayButtonOutput: false,
      });
    }
  };
  tinyslider();

  var sitePlusMinus = function () {
    var value,
      quantity = document.getElementsByClassName("quantity-container");

    function createBindings(quantityContainer) {
      var quantityAmount =
        quantityContainer.getElementsByClassName("quantity-amount")[0];
      var increase = quantityContainer.getElementsByClassName("increase")[0];
      var decrease = quantityContainer.getElementsByClassName("decrease")[0];
      increase.addEventListener("click", function (e) {
        increaseValue(e, quantityAmount);
      });
      decrease.addEventListener("click", function (e) {
        decreaseValue(e, quantityAmount);
      });
    }

    function init() {
      for (var i = 0; i < quantity.length; i++) {
        createBindings(quantity[i]);
      }
    }

    function increaseValue(event, quantityAmount) {
      value = parseInt(quantityAmount.value, 10);

      console.log(quantityAmount, quantityAmount.value);

      value = isNaN(value) ? 0 : value;
      value++;
      quantityAmount.value = value;
    }

    function decreaseValue(event, quantityAmount) {
      value = parseInt(quantityAmount.value, 10);

      value = isNaN(value) ? 0 : value;
      if (value > 0) value--;

      quantityAmount.value = value;
    }

    init();
  };
  sitePlusMinus();
})();

// class BootstrapModal {
//   constructor(title, bodyContent) {
//     // Create the modal structure
//     this.modal = document.createElement("div");
//     this.modal.className = "modal fade";
//     this.modal.setAttribute("tabindex", "-1");
//     this.modal.setAttribute("role", "dialog");
//     this.modal.innerHTML = `
//           <div class="modal-dialog" role="document">
//               <div class="modal-content">
//                   <div class="modal-header">
//                       <h5 class="modal-title">${title}</h5>
//                       <button type="button" class="close" data-dismiss="modal" aria-label="Close">
//                           <span aria-hidden="true">&times;</span>
//                       </button>
//                   </div>
//                   <div class="modal-body">
//                       <p>${bodyContent}</p>
//                   </div>
//                   <div class="modal-footer">
//                       <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
//                       <button type="button" class="btn btn-primary">Save changes</button>
//                   </div>
//               </div>
//           </div>
//       `;
//
//     document.body.appendChild(this.modal);
//
//     this.bootstrapModal = new bootstrap.Modal(this.modal, {
//       backdrop: "static", // Makes sure clicking outside the modal won't close it
//       keyboard: true, // Allows closing the modal with the escape key
//     });
//   }
//
//   show() {
//     this.bootstrapModal.show();
//   }
//
//   hide() {
//     this.bootstrapModal.hide();
//   }
//
//   remove() {
//     this.hide();
//     this.modal.addEventListener("hidden.bs.modal", () => {
//       this.modal.remove();
//     });
//   }
// }
