import { Component, OnInit } from '@angular/core';
import { RestApiService } from "../shared/rest-api.service";
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.css']
})
export class ItemEditComponent implements OnInit {
  id = this.actRoute.snapshot.params['id'];
  itemData: any = {};

  constructor(
    public restApi: RestApiService,
    public actRoute: ActivatedRoute,
    public router: Router
  ) {
  }

  ngOnInit() {
    this.restApi.getItem(this.id).subscribe((data: {}) => {
      this.itemData = data;
    })
  }
  updateItemDeposit() {
    if (window.confirm('Are you sure, you want to update this item?')) {
      this.restApi.depositItem(this.id, this.itemData).subscribe(data => {
        this.router.navigate(['/items-list'])
      })
    }
  }

  updateItemWithdrawal() {
    if (window.confirm('Are you sure, you want to update this item?')) {
      this.restApi.withdrawalItem(this.id, this.itemData).subscribe(data => {
        this.router.navigate(['/items-list'])
      })
    }
  }

}
